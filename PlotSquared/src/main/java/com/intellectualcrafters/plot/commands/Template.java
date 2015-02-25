////////////////////////////////////////////////////////////////////////////////////////////////////
// PlotSquared - A plot manager and world generator for the Bukkit API                             /
// Copyright (c) 2014 IntellectualSites/IntellectualCrafters                                       /
//                                                                                                 /
// This program is free software; you can redistribute it and/or modify                            /
// it under the terms of the GNU General Public License as published by                            /
// the Free Software Foundation; either version 3 of the License, or                               /
// (at your option) any later version.                                                             /
//                                                                                                 /
// This program is distributed in the hope that it will be useful,                                 /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                                  /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                   /
// GNU General Public License for more details.                                                    /
//                                                                                                 /
// You should have received a copy of the GNU General Public License                               /
// along with this program; if not, write to the Free Software Foundation,                         /
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA                               /
//                                                                                                 /
// You can contact us via: support@intellectualsites.com                                           /
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.intellectualcrafters.plot.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.intellectualcrafters.plot.PlotSquared;
import com.intellectualcrafters.plot.config.C;
import com.intellectualcrafters.plot.config.Configuration;
import com.intellectualcrafters.plot.object.FileBytes;
import com.intellectualcrafters.plot.object.PlotManager;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.object.PlotWorld;
import com.intellectualcrafters.plot.util.BlockManager;
import com.intellectualcrafters.plot.util.MainUtil;

public class Template extends SubCommand {
    public Template() {
        super("template", "plots.admin", "Create or use a world template", "template", "", CommandCategory.DEBUG, false);
    }

    @Override
    public boolean execute(final PlotPlayer plr, final String... args) {
        if (args.length != 2) {
            MainUtil.sendMessage(plr, C.COMMAND_SYNTAX, "/plot template <import|export> <world>");
            return false;
        }
        final String world = args[1];
        switch (args[0].toLowerCase()) {
            case "import": {
                // TODO import template
                MainUtil.sendMessage(plr, "TODO");
                return true;
            }
            case "export": {
                final PlotWorld plotworld = PlotSquared.getPlotWorld(world);
                if (!BlockManager.manager.isWorld(world) || (plotworld == null)) {
                    MainUtil.sendMessage(plr, C.NOT_VALID_PLOT_WORLD);
                    return false;
                }
                PlotManager manager = PlotSquared.getPlotManager(world);
                manager.export(plotworld);
                MainUtil.sendMessage(plr, "Done!");
            }
        }
        // TODO allow world settings (including schematics to be packed into a single file)
        // TODO allow world created based on these packaged files
        return true;
    }
    
    public static byte[] getBytes(PlotWorld plotworld) {
        YamlConfiguration config = new YamlConfiguration();
        ConfigurationSection section = config.getConfigurationSection("");
        plotworld.saveConfiguration(section);
        return config.saveToString().getBytes();
    }
    
    public static boolean zipAll(final String world, Set<FileBytes> files) {
        try {
            File output = new File(PlotSquared.IMP.getDirectory() + File.separator + "templates");
            output.mkdirs();
            FileOutputStream fos = new FileOutputStream(output + File.separator + world + ".template");
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            for (FileBytes file : files) {
                ZipEntry ze = new ZipEntry(file.path);
                zos.putNextEntry(ze);
                zos.write(file.data);
            }
            zos.closeEntry();
            zos.close();
            return true;
        } catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
