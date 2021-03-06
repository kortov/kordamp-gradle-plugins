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
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.kordamp.gradle.plugin.base.ProjectConfigurationExtension

/**
 * @author Andres Almiray
 * @since 0.8.0
 */
@CompileStatic
@Canonical
class Source extends AbstractAggregateFeature {
    static final String PLUGIN_ID = 'org.kordamp.gradle.source-jar'

    Source(ProjectConfigurationExtension config, Project project) {
        super(config, project, PLUGIN_ID, 'source')
    }

    @Override
    protected void populateMapDescription(Map<String, Object> map) {
    }

    void normalize() {
        if (!enabledSet) {
            if (isRoot()) {
                if (project.childProjects.isEmpty()) {
                    setEnabled(isApplied())
                } else {
                    setEnabled(project.childProjects.values().any { p -> isApplied(p) })
                }
            } else {
                setEnabled(isApplied())
            }
        }
    }
}
