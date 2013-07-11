/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.tests.runner.internal;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.testng.TestNG;

import com.peergreen.tests.runner.Runner;
import com.peergreen.tests.runner.internal.testng.TestListener;
import com.peergreen.tests.runner.internal.testng.TestNGObjectFactory;

@Component
@Instantiate
@Provides(specifications=Runner.class)
public class TestRunner implements Runner, TestRunnerMXBean {

    private final MBeanServer mBeanServer;

    private final ObjectName objectName;


    private final List<TestListener> listeners;

    public TestRunner() throws MalformedObjectNameException {
        this.objectName = new ObjectName("peergreen:type=ArquillianTestRunner");
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        this.listeners = new ArrayList<>();
    }


    @Validate
    public void start() {
        try {
            mBeanServer.registerMBean(this, objectName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    public void stop() {
        try {
            mBeanServer.unregisterMBean(objectName);
        } catch (MBeanRegistrationException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void register(Object instance) {
        runTest(instance);


    }

    @Override
    public void unregister(Object componentTester) {
    }



    public void runTest(Object instance) {

        // build instance of TestNG
        TestNG testNG = new TestNG();

        TestListener testListener = new TestListener(instance.getClass().getName());
        listeners.add(testListener);
        testNG.addListener(testListener);

        testNG.setDefaultSuiteName("Peergreen Test Suite on ".concat(instance.getClass().getName()));

        // Define class of the test
        testNG.setTestClasses(new Class[] {instance.getClass()});

        // Set our own factory in order to send existing instances
        testNG.setObjectFactory(new TestNGObjectFactory(instance));

        // run
        testNG.run();

    }


    @Override
    public boolean check(int componentsNumber) {
        // ensure all results are ok
        boolean success = true;
        for (TestListener testListener : listeners) {
            if (testListener.hasErrors()) {
                success = false;
            }
        }
        if (listeners.size() != componentsNumber) {
            success = false;
            System.err.println("Expecting " + componentsNumber + "' but there are only '" + listeners.size() + "' tests result");
        }

        listeners.clear();

        return success;
    }

}
