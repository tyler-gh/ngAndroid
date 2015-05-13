/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.davityle.ngprocessor.manifestfinders;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

public class OptionsHelper {

	enum Option {
		ANDROID_MANIFEST_FILE("androidManifestFile", null);

		private String key;
		private String defaultValue;

		Option(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}

		public String getKey() {
			return key;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
	}

	private final Map<String, String> options;

	public OptionsHelper(ProcessingEnvironment processingEnvironment) {
		options = processingEnvironment.getOptions();
	}

	public String getAndroidManifestFile() {
		return getString(Option.ANDROID_MANIFEST_FILE);
	}


	private String getString(Option option) {
		String key = option.getKey();
		if (options.containsKey(key)) {
			return options.get(key);
		} else {
			return option.getDefaultValue();
		}
	}

	private boolean getBoolean(Option option) {
		return Boolean.valueOf(getString(option));
	}

}