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

import org.testng.IObjectFactory2;

public class TestNGObjectFactory implements IObjectFactory2 {

    /**
     *
     */
    private static final long serialVersionUID = 6038965206738362635L;

    private final Object instance;


    public TestNGObjectFactory(Object instance) {
        this.instance = instance;
    }


    @Override
    public Object newInstance(Class<?> clazz) {
        if (clazz.isAssignableFrom(instance.getClass())) {
            return instance;
        }
        return null;
    }

}
