/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.kerby.kerberos.kerb.client;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class Krb5Parser {
    private File krb5conf;
    private Map<String, Object> items;

    public Krb5Parser(File confFile) {
        krb5conf = confFile;
        items = null;
    }


    /**
     * main function for test.
     * because of using hashmap:
     *  sections are disordered.
     *  entries are disordered.
     *  for the same key, there can be only one value.
     * @throws IOException
     */
    /*public static void main (String[] args) throws IOException {
        Krb5Parser k = new Krb5Parser(new File ("/krb5.conf"));
        k.load();
        k.getSections();
        //k.dump();
        //Map<String, Object> m = k.getSection("realms");
        //k.printEntry(m, 0);
    }*/

    public void load() throws IOException {
        InputStream is = Krb5confLoader.class.getResourceAsStream("/" + krb5conf.toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        items = new HashMap<String, Object> ();

        String originLine = br.readLine();
        while (originLine != null) {
            String line = originLine.trim();
            if (line.startsWith("#")) {//comments
                originLine = br.readLine();
            }
            else if (line.startsWith("[")) {//section begin
                insertSections(line, br, items);//add a section
                originLine = br.readLine();
            }
            else {
                System.out.println("watch out! " + originLine);
                originLine = br.readLine();
            }
        }
    }

    public List<String> getSections() {
        List<String> al = new ArrayList<String>();
        Iterator iter = items.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            al.add(key);
        }
        return al;
    }

    public Map<String, Object> getSection(String sectionName) {
        Map<String, Object> sections = (HashMap)items.get(sectionName);
        return sections;
    }

    public void dump() {//print to console
        printSection(items);
    }

    private void printSection (Map<String, Object> map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            System.out.println("[" + key + "]");

            if (value instanceof Map) {
                int count = 0;
                printEntry((Map)value, count);
            }
            else {
                System.out.println("error format!");
                System.exit(0);//???
            }
        }
    }

    private void printEntry (Map<String, Object> map, int count) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
           for (int i = 0; i < count; i++) {
                System.out.print("\t");
           }
            if (value instanceof String) {
                System.out.println(key + " = " + (String)value);
            }
            if (value instanceof Map) {
                System.out.println(key + " = {");
                //count++;
                printEntry((Map)value, count + 1);
                for (int i = 0; i < count; i++) {
                    System.out.print("\t");
                }
                System.out.println("}");
            }
        }
    }

    private void insertSections (String line, BufferedReader br, Map<String, Object> items) throws IOException {
        //System.out.println("lineS: " + line);
        while (line.startsWith("[")) {
            //System.out.println("encounter a section2");
            //HashMap<String, Object> section = new HashMap<String, Object> ();
            //it is the same as items
            String sectionName = line.substring(1, line.length() - 1);
            //System.out.println(sectionName);
            Map<String, Object> entries = new HashMap<String, Object> ();
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertEntries(line, br, entries);//obtain all the entries of a section
                items.put(sectionName, entries);//add a section to items.
                //watch out that sections are disordered!!!
            }
            if (line == null) {
                //System.out.println ("-----------------line == null--------------------" );
                break;
            }
        }
    }

    /*recursively go through the key-value pairs of a section*/
    private String insertEntries (String line, BufferedReader br, Map<String, Object> entries) throws IOException {
        //System.out.println("line: " + line);
        if (line == null) {
            return line;
        }
        if (line.startsWith("[")) {
            //System.out.println("encounter a section1");
            return line;
        }
        if (line.startsWith("}")) {
            //System.out.println("suppose to be here");
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                //line = insertKV(line, br, hm);
            }
            return line;
        }
        if (line.length() == 0) {
            line = br.readLine();
            if (line != null) {
                //System.out.println("an enter");
                line = line.trim();
                line = insertEntries(line, br, entries);
            }
            return line;
        }
        /*some special cases above*/
        //System.out.println ("before kv: " + line);
        String[] kv = line.split("=");
        if (kv.length > 2) {
            System.out.println("key_value error!");//report error
            System.exit(0);//////////////////
        }
        kv[0] = kv[0].trim();
        kv[1] = kv[1].trim();

        if (kv[1].startsWith("{")) {//multi key-value
            //Map<String, Object> multiEntry = new HashMap<String, Object> ();
            //key = kv[0], kv[1] = "{"
            //value is a hashmap in next line
            Map<String, Object> meValue = new HashMap<String, Object> ();
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertEntries(line, br, meValue);
                entries.put(kv[0], meValue);
                line = insertEntries(line, br, entries);
            }
        }
        else {//single key-value
            entries.put(kv[0], kv[1]);
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertEntries(line, br, entries);
            }
        }
        return line;
    }
}