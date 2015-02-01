package com.nashesz.cudroid.DataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 31/01/2015.
 */
public class CUContainer {
    protected int __level__;
    protected int __checked__;
    protected int _name;
    protected List<CUContainer> _list;
    
    public CUContainer()
    {
        _list = new ArrayList<CUContainer>();
    }
}
