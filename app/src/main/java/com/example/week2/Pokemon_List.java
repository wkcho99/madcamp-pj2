package com.example.week2;

import java.util.HashMap;
import java.util.Map;

public class Pokemon_List{
    public static HashMap<Integer,String> map = new HashMap<Integer,String>(){
        {
            put(1,"모부기");
            put(2,"수풀부기");
            put(3,"토대부기");
            put(4,"불꽃숭이");
            put(5,"파이숭이");
            put(6,"초염몽");
            put(7,"팽도리");
            put(8,"팽태자");
            put(9,"앰페르트");
        }
    };

}
//public enum Pokemon_List {
//    모부기(1),수풀부기(2),토대부기(3),불꽃숭이(4),파이숭이(5),
//    초염몽(6),팽도리(7),팽태자(8),앰페르트(9);
//    private final int value;
//    Pokemon_List(int value) {this.value = value;}
//    public int getValue(){return value;}
//    public static final Map<String, Pokemon_List> map = new HashMap<>();
//    static {
//        for (Pokemon_List po : Pokemon_List.values()) {
//            map.put(Integer.toString(po.getValue()), po);
//        }
//    }
//    public static Pokemon_List getpobyid(String id)
//    { return map.get(id); }
//
//}
