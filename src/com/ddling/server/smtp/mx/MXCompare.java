/*
 * Copyright (C) 2014 lingdongdong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ddling.server.smtp.mx;

import org.xbill.DNS.MXRecord;

import java.util.Comparator;

/**
 * Created by ddling on 2014/12/25.
 */
public class MXCompare implements Comparator {

    @Override
    public int compare(Object arg0, Object arg1) {
        return Integer.compare(((MXRecord) arg0).getPriority(), ((MXRecord) arg1).getPriority());
    }
}
