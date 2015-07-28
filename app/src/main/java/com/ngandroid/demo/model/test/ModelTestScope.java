/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ngandroid.demo.model.test;

import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

/**
* Created by tyler on 3/23/15.
*/
@NgScope(name="ModelTestScope")
public class ModelTestScope {
//  TODO throw an error in this situation @NgModel
    public TestSetterRequired testSetterRequired;
    @NgModel
    public TestJsonModel testJsonModel;
    @NgModel
    public TestSubModel testSubModel;
    @NgModel
    public TestClass TestClass;
}
