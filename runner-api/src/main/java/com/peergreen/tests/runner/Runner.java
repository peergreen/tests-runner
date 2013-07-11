/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.tests.runner;;

/**
 * Interface used by the tests to register on the remote runner
 * @author Florent Benoit
 */
public interface Runner {

    /**
     * Register a component and all tests will be executed on this given instance
     * @param componentTester instance to be tested
     */
    void register(Object componentTester);

    /**
     * Unregister the given instance
     * @param componentTester
     */
    void unregister(Object componentTester);
}
