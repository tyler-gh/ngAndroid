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

import com.github.davityle.ngprocessor.manifestfinders.FileHelper.FileHolder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.annotation.processing.ProcessingEnvironment;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AndroidManifestFinder {

	private static final int MAX_PARENTS_FROM_SOURCE_FOLDER = 10;

	private final ProcessingEnvironment processingEnv;
	private final OptionsHelper optionsHelper;

	public AndroidManifestFinder(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		optionsHelper = new OptionsHelper(processingEnv);
	}

	public Option<String> extractAndroidManifest() {
		Option<File> androidManifestFileOption = findManifestFile();

		if (androidManifestFileOption.isAbsent()) {
			return Option.absent();
		}

		File androidManifestFile = androidManifestFileOption.get();


		return parse(androidManifestFile);
	}

	private Option<File> findManifestFile() {
		String androidManifestFile = optionsHelper.getAndroidManifestFile();
        if (androidManifestFile != null) {
			return findManifestInSpecifiedPath(androidManifestFile);
		} else {
			return findManifestInParentsDirectories();
		}
	}

	private Option<File> findManifestInSpecifiedPath(String androidManifestPath) {
		File androidManifestFile = new File(androidManifestPath);
		if (!androidManifestFile.exists()) {
			return Option.absent();
		}
		return Option.of(androidManifestFile);
	}

	/**
	 * We use a dirty trick to find the AndroidManifest.xml file, since it's not
	 * available in the classpath. The idea is quite simple : create a fake
	 * class file, retrieve its URI, and start going up in parent folders to
	 * find the AndroidManifest.xml file. Any better solution will be
	 * appreciated.
	 */
	private Option<File> findManifestInParentsDirectories() {
		Option<FileHolder> projectRootHolderOption = FileHelper.findRootProjectHolder(processingEnv);

        if (projectRootHolderOption.isAbsent()) {
			return Option.absent();
		}

		FileHolder projectRootHolder = projectRootHolderOption.get();
		File projectRoot = projectRootHolder.projectRoot;

		File androidManifestFile = new File(projectRoot, "AndroidManifest.xml");
		for (int i = 0; i < MAX_PARENTS_FROM_SOURCE_FOLDER; i++) {
			if (androidManifestFile.exists()) {
				break;
			}
            androidManifestFile = new File(projectRoot,"src" + File.separator + "main" + File.separator + "AndroidManifest.xml");
            if (androidManifestFile.exists()) {
                break;
            }
            if (projectRoot.getParentFile() != null) {
                projectRoot = projectRoot.getParentFile();
                androidManifestFile = new File(projectRoot, "AndroidManifest.xml");
            } else {
                break;
            }
		}

		if (!androidManifestFile.exists()) {
            return Option.absent();
		}

		return Option.of(androidManifestFile);
	}

	private Option<String> parse(File androidManifestFile) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		Document doc;
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(androidManifestFile);
		} catch (Exception e) {
			e.printStackTrace();
			return Option.absent();
		}

		Element documentElement = doc.getDocumentElement();
		documentElement.normalize();

		return Option.of(documentElement.getAttribute("package"));
	}
}
