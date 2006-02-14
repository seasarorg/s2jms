/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jms.container.impl;

import java.util.Arrays;

/**
 * @author Kenichiro Murata
 * 
 */
public class MapTest {

    private boolean invalid;
    private byte id;
    private byte[] relIds;
    private char head;
    private double weight;
    private float hight;
    private int serialNumber;
    private long extendSerialNumber;
    private short extendId;
    private String name;
    private Object obj;

    public short getExtendId() {
        return extendId;
    }

    public void setExtendId(short extendId) {
        this.extendId = extendId;
    }

    public long getExtendSerialNumber() {
        return extendSerialNumber;
    }

    public void setExtendSerialNumber(long extendSerialNumber) {
        this.extendSerialNumber = extendSerialNumber;
    }

    public char getHead() {
        return head;
    }

    public void setHead(char head) {
        this.head = head;
    }

    public float getHight() {
        return hight;
    }

    public void setHight(float hight) {
        this.hight = hight;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public byte[] getRelIds() {
        return relIds;
    }

    public void setRelIds(byte[] relIds) {
        this.relIds = relIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (null != obj) {
            MapTest tmp = (MapTest) obj;
            return (this.extendId == tmp.getExtendId()
                    && this.extendSerialNumber == tmp.getExtendSerialNumber()
                    && this.head == tmp.getHead() && this.hight == tmp.getHight()
                    && this.id == tmp.getId() && this.name.equals(tmp.getName())
                    && this.invalid == tmp.isInvalid() && this.obj.equals(tmp.getObj())
                    && Arrays.equals(this.relIds, tmp.getRelIds())
                    && this.serialNumber == tmp.getSerialNumber() && this.weight == tmp.getWeight());
        }
        return super.equals(obj);
    }

}
