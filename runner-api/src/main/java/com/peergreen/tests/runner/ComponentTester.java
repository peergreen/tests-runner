/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.tests.runner;

import java.lang.management.ManagementFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import junit.framework.Assert;

/**
 * Defines the super class for tests that needs the remote runner
 * @author Florent Benoit
 */
public class ComponentTester {


    public void check() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName runnerMBean;
        try {
            runnerMBean = new ObjectName("peergreen:type=ArquillianTestRunner");
        } catch (MalformedObjectNameException e) {
            throw new IllegalStateException("Cannot get the Test Runner", e);
        }
        if (!mBeanServer.isRegistered(runnerMBean)) {
            throw new IllegalStateException("Unable to get the runner");
        }

        int numberOfTests = Integer.valueOf(System.getProperty("peergreen.arquillian.generated.components", "0"));
        try {
            boolean success = (boolean) mBeanServer.invoke(runnerMBean, "check", new Object[] { numberOfTests }, new String[] {"int"});
            if (!success) {
                Assert.fail("Error in the remote tests");
            }
        } catch (InstanceNotFoundException | ReflectionException | MBeanException e) {
            throw new IllegalStateException("Error while launching tests", e);
        }

    }

}
