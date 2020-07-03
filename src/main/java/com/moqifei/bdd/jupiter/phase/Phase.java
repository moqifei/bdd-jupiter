/*
 * Copyright 2018 Contributors

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moqifei.bdd.jupiter.phase;

import com.moqifei.bdd.jupiter.modle.Given;
import com.moqifei.bdd.jupiter.modle.Then;
import com.moqifei.bdd.jupiter.modle.When;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a phase in a scene - {@link Given}, {@link When}, {@link Then}.
 * Optionally, a phase can holds subPhase such as additional "and" conditions etc.
 */
public abstract class Phase implements Divisible{
    private String description;
    private List<SubPhase> subPhases;

    Phase(String description) {
        this.description = description;
        this.subPhases = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public List<SubPhase> geSubPhases() {
        return subPhases;
    }
}
