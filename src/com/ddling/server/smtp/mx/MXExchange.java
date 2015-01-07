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

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ddling on 2014/12/25.
 */
public class MXExchange {

    public static ArrayList<MXRecord> mxLookup(String server) throws Exception {

        ArrayList<MXRecord> mxServers = new ArrayList<MXRecord>();

        Lookup lookup = new Lookup(server, Type.MX);
        Record[] records = lookup.run();

        for (int i = 0; i < records.length; i++) {
            mxServers.add((MXRecord)records[i]);
        }

        Collections.sort(mxServers, new MXCompare());
        return mxServers;
    }

    public static String getMxServer(String server) throws Exception {

        ArrayList<MXRecord> servers = MXExchange.mxLookup(server);
        if (servers.isEmpty()) {
            return null;
        }

        String serverName = servers.get(0).getTarget().toString();
        return serverName.substring(0, serverName.length() - 1);
    }

    public static void main(String[] args) {
        String serverName = "gmail.com";
        try {
            String name = getMxServer(serverName);
            System.out.print(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
