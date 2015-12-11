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

public class Krb5confLoader {
    class Value {
        String val;
        HashMap<String, Value> next_kv;
    }
    class Section {
        String name;
        HashMap<String, Value> kv;
    }

    @Test
    public void test() throws Exception {
        InputStream is = Krb5confLoader.class.getResourceAsStream("/krb5.conf");
        // Do the work here.
        ArrayList<Section> al = new ArrayList<Section> ();//section list

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String originLine = br.readLine();
        while (originLine != null) {
            String line = originLine.trim();
            if (line.startsWith("#")) {//comments
                //System.out.println(line);
                originLine = br.readLine();
            }
            else if (line.startsWith("[")) {//section begin
                insertSection(line, br, al);//recursively
                //System.out.println("return from recursive: " + line);
                originLine = br.readLine();
                //System.out.println("return from recursive: " + originLine);
                //where is the br??
            }
            else {
                System.out.println("watch out! " + originLine);
                originLine = br.readLine();
            }
        }

        //print the sections
        printSection (al);

    }


    private void insertSection (String line, BufferedReader br, ArrayList<Section> al) throws IOException {
        //System.out.println("lineS: " + line);
        while (line.startsWith("[")) {
            //System.out.println("encounter a section2");
            Section section = new Section ();//create a new section
            String sectionName = line.substring(1, line.length() - 1);
            section.name = sectionName;//section name
            System.out.println(sectionName);
            section.kv = new HashMap<String, Value>();
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertKV(line, br, section.kv);
                al.add(section);
            }
            if (line == null) {
                System.out.println("line == null");
                break;
            }
        }
    }

    /*recursively go through the key-value pairs of a section*/
    private String insertKV (String line, BufferedReader br, HashMap<String, Value> hm) throws IOException {
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
                line = insertKV(line, br, hm);
            }
            return line;
        }
        //System.out.println ("before kv: " + line);
        String[] kv = line.split("=");
        if (kv.length > 2) {
            System.out.println("key_value error!");//report error
            System.exit(0);//////////////////
        }
        kv[0] = kv[0].trim();
        kv[1] = kv[1].trim();

        if (kv[1].startsWith("{")) {//multi key-value
            Value multi = new Value ();
            multi.val = null;
            multi.next_kv = new HashMap<String, Value>();
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertKV(line, br, multi.next_kv);
                //System.out.println("0back: " + multi.next_kv.size());
                hm.put(kv[0], multi);
                //line = br.readLine();
                //if (line != null) {
                    //line = line.trim();
                line = insertKV(line, br, hm);
                //System.out.println("1back: " + multi.next_kv.size());
                //}

            }
        }
        else {//single key-value
            Value single = new Value ();
            single.val = kv[1];
            single.next_kv = null;
            hm.put(kv[0], single);
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                line = insertKV(line, br, hm);
            }
            //System.out.println("after recur:" + line);
        }
        //System.out.println("out of a level");
        return line;
    }


    private void printSection (ArrayList<Section> al) {
        System.out.println("========================");
        for (int i = 0; i < al.size(); i++) {
            System.out.println("Section: [" + al.get(i).name + "]");
            HashMap<String, Value> hm = al.get(i).kv;
            Iterator it = hm.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String)entry.getKey();
                Value value = (Value)entry.getValue();
                if (value.next_kv == null) {
                    System.out.println(key + " = " + value.val);
                }
                else {
                    System.out.println(key + " = {");
                    HashMap<String, Value> hm1 = value.next_kv;
                    Iterator it1 = hm1.entrySet().iterator();
                    while (it1.hasNext()) {
                        Map.Entry entry1 = (Map.Entry) it1.next();
                        String key1 = (String)entry1.getKey();
                        Value value1 = (Value)entry1.getValue();
                        if (value1.next_kv == null) {
                            System.out.println("\t" + key1 + " = " + value1.val);
                        }
                        else {
                            System.out.println("\t" + key1 + " = {");
                            HashMap<String, Value> hm2 = value1.next_kv;
                            Iterator it2 = hm2.entrySet().iterator();
                            while (it2.hasNext()) {
                                Map.Entry entry2 = (Map.Entry) it2.next();
                                String key2 = (String) entry2.getKey();
                                Value value2 = (Value) entry2.getValue();
                                if (value2.next_kv == null) {
                                    System.out.println("\t\t" + key2 + " = " + value2.val);
                                } else {
                                    System.out.println("\t\t" + key2 + " = {");
                                    System.out.println("\t\t}");
                                }
                            }
                            System.out.println("\t" + "}");
                        }
                    }




                    System.out.println("}");

                }
            }
        }
    }
}
