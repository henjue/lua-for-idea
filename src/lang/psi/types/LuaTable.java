/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.sylvanaar.idea.Lua.lang.psi.types;

import com.intellij.openapi.diagnostic.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 9/18/11
 * Time: 3:08 AM
 */
public class LuaTable extends LuaType {
    Logger log = Logger.getInstance("Lua.LuaTable");
    
    private Map<String, LuaType> hash = new HashMap<String, LuaType>();

    public LuaTable(String name) {
        super(name);
        log.debug("New Table: " + name);
    }

    public void addPossibleElement(String key, LuaType type) {
        log.debug("New Element of Table: " + toString() + " " + key + " " + type);
        hash.put(key, type);
    }

    public Map<?,?> getFieldSet() {
        return hash;
    }
}
