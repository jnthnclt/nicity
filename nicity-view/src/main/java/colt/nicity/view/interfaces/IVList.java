/*
 * IVList.java.java
 *
 * Created on 01-03-2010 01:31:40 PM
 *
 * Copyright 2010 Jonathan Colt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colt.nicity.view.interfaces;

import colt.nicity.core.collection.IBackcall;

/**
 *
 * @author Administrator
 */
public interface IVList extends IView {

    /**
     *
     * @return
     */
    public IListController getListController();

    /**
     *
     * @param _controller
     */
    public void setListController(IListController _controller);

    /**
     *
     * @return
     */
    public int getRowsColums();

    /**
     *
     * @param _rows_colums
     */
    public void setRowsColums(int _rows_colums);

    /**
     *
     * @param _backcall
     */
    public void setBackcall(IBackcall _backcall);

    /**
     *
     * @return
     */
    public boolean isVertical();

    /**
     *
     * @return
     */
    public boolean isHorizontal();
}

