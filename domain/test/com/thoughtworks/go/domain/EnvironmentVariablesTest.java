/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.domain;

import com.thoughtworks.go.config.EnvironmentVariableConfig;
import com.thoughtworks.go.config.EnvironmentVariablesConfig;
import com.thoughtworks.go.security.GoCipher;
import com.thoughtworks.go.util.command.EnvironmentVariableContext;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EnvironmentVariablesTest {

    @Test
    public void add_shouldAddEnvironmentVariable() {
        final EnvironmentVariables environmentVariables = new EnvironmentVariables();
        assertThat(environmentVariables, hasSize(0));

        environmentVariables.add("foo", "bar");

        assertThat(environmentVariables, hasSize(1));
        assertThat(environmentVariables, contains(new EnvironmentVariable("foo", "bar")));
    }

    @Test
    public void toEnvironmentVariables_shouldConvertEnvironmentVariablesConfigToEnvironmentVariable() {
        final EnvironmentVariablesConfig environmentVariableConfigs = new EnvironmentVariablesConfig(Arrays.asList(
                new EnvironmentVariableConfig("foo", "bar"),
                new EnvironmentVariableConfig(new GoCipher(), "baz", "car", true)
        ));

        final EnvironmentVariables environmentVariables = EnvironmentVariables.toEnvironmentVariables(environmentVariableConfigs);

        assertThat(environmentVariables, containsInAnyOrder(
                new EnvironmentVariable("foo", "bar", false),
                new EnvironmentVariable("baz", "car", true)
        ));
    }

    @Test
    public void addTo_shouldAddEnvironmentVariablesToEnvironmentVariableContext() {
        final EnvironmentVariableContext environmentVariableContext = mock(EnvironmentVariableContext.class);
        final EnvironmentVariables environmentVariables = new EnvironmentVariables(
                new EnvironmentVariable("foo", "bar"),
                new EnvironmentVariable("baz", "car", true)
        );

        environmentVariables.addTo(environmentVariableContext);

        verify(environmentVariableContext, times(1)).setProperty("foo", "bar", false);
        verify(environmentVariableContext, times(1)).setProperty("baz", "car", true);
    }

    @Test
    public void addToIfExists_shouldAddEnvironmentVariableToEnvironmentVariableContext() {
        final EnvironmentVariableContext environmentVariableContext = mock(EnvironmentVariableContext.class);
        final EnvironmentVariables environmentVariables = new EnvironmentVariables(
                new EnvironmentVariable("foo", "bar"),
                new EnvironmentVariable("baz", "car", true)
        );

        when(environmentVariableContext.hasProperty("foo")).thenReturn(false);
        when(environmentVariableContext.hasProperty("baz")).thenReturn(true);

        environmentVariables.addToIfExists(environmentVariableContext);

        verify(environmentVariableContext, times(0)).setProperty("foo", "bar", false);
        verify(environmentVariableContext, times(1)).setProperty("baz", "car", true);
    }
}