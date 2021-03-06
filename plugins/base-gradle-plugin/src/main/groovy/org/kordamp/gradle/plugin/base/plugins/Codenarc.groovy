/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2018-2020 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.gradle.plugin.base.plugins

import groovy.transform.Canonical
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeNarc
import org.kordamp.gradle.plugin.base.ProjectConfigurationExtension

/**
 * @author Andres Almiray
 * @since 0.31.0
 */
@CompileStatic
@Canonical
class Codenarc extends AbstractQualityFeature {
    static final String PLUGIN_ID = 'org.kordamp.gradle.codenarc'

    File configFile
    int maxPriority1Violations
    int maxPriority2Violations
    int maxPriority3Violations

    Codenarc(ProjectConfigurationExtension config, Project project) {
        super(config, project, PLUGIN_ID, 'codenarc')
        toolVersion = '1.5'
    }

    @Override
    protected void populateMapDescription(Map<String, Object> map) {
        super.populateMapDescription(map)
        map.configFile = this.configFile
        map.maxPriority1Violations = this.maxPriority1Violations
        map.maxPriority2Violations = this.maxPriority2Violations
        map.maxPriority3Violations = this.maxPriority3Violations
    }

    @Override
    void normalize() {
        if (null == configFile) {
            File file = project.rootProject.file("config/codenarc/${project.name}.xml")
            if (!file.exists()) {
                file = project.rootProject.file('config/codenarc/codenarc.xml')
            }
            configFile = file
        }

        super.normalize()
    }

    void copyInto(Codenarc copy) {
        super.copyInto(copy)
        copy.configFile = configFile
        copy.maxPriority1Violations = maxPriority1Violations
        copy.maxPriority2Violations = maxPriority2Violations
        copy.maxPriority3Violations = maxPriority3Violations
        copy.toolVersion = toolVersion
        aggregate.copyInto(copy.aggregate)
    }

    static void merge(Codenarc o1, Codenarc o2) {
        AbstractQualityFeature.merge(o1, o2)
        o1.configFile = o1.configFile ?: o2.configFile
        o1.maxPriority1Violations = o1.maxPriority1Violations ?: o2.maxPriority1Violations
        o1.maxPriority2Violations = o1.maxPriority2Violations ?: o2.maxPriority2Violations
        o1.maxPriority3Violations = o1.maxPriority3Violations ?: o2.maxPriority3Violations
    }

    @CompileDynamic
    void applyTo(CodeNarc codenarcTask) {
        String sourceSetName = (codenarcTask.name - 'codenarc').uncapitalize()
        sourceSetName = sourceSetName == 'allCodenarc' ? project.name : sourceSetName
        sourceSetName = sourceSetName == 'aggregateCodenarc' ? 'aggregate' : sourceSetName
        codenarcTask.enabled = enabled && configFile.exists()
        codenarcTask.configFile = configFile
        codenarcTask.maxPriority1Violations = maxPriority1Violations
        codenarcTask.maxPriority2Violations = maxPriority2Violations
        codenarcTask.maxPriority3Violations = maxPriority3Violations
        codenarcTask.ignoreFailures = ignoreFailures
        codenarcTask.reports.html.enabled = true
        codenarcTask.reports.xml.enabled = true
        codenarcTask.reports.html.destination = project.layout.buildDirectory.file("reports/codenarc/${sourceSetName}.html").get().asFile
        codenarcTask.reports.xml.destination = project.layout.buildDirectory.file("reports/codenarc/${sourceSetName}.xml").get().asFile
    }
}
