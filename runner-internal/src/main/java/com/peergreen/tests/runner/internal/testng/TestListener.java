/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.tests.runner.internal.testng;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.reporters.TextReporter;

public class TestListener extends TextReporter implements ITestListener {

    private boolean errors;

    public TestListener(String testName) {
        super(testName, 2);
    }


    @Override
    public void onFinish(ITestContext context) {

        // Change output
        PrintStream old = System.out;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(byteArrayOutputStream);

        try {
            System.setOut(newOut);
            // build report
            super.onFinish(context);
        }  finally {
            System.setOut(old);
        }

        if (context.getFailedTests().size() > 0 || context.getSkippedTests().size() > 0) {
            errors = true;
            System.out.println(new String(byteArrayOutputStream.toByteArray()));
        }
    }


    public boolean hasErrors() {
        return errors;
    }



}
