package com.jonathancolt.nicity.io;

/*
 * #%L
 * nicity-io
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.io.IFiler;
import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.UString;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UFile {
    // Splits a PATH (with ; as separators) and fixes slashes for platform
    public static String[] toPaths(String _classPath) {
        if (_classPath == null) {
            return new String[0];
        }
        String[] paths = UString.toStringArray(_classPath, ";");
        for (int i = 0; i < paths.length; i++) {
            paths[i] = fixSlashes(paths[i]);
        }
        return paths;
    }
    public static String fixSlashes(String _path) {
        if (_path == null) {
            return "";
        }
        final StringBuffer result = new StringBuffer(_path);
        for (int i = 0; i < result.length(); i++) {
            fixSlash(result, i);
        }
        return result.toString();
    }
    private static boolean fixSlash(StringBuffer buffer, int pos) {
        if (buffer.charAt(pos) == '/' || buffer.charAt(pos) == '\\') {
            buffer.setCharAt(pos, File.separatorChar);
            return true;
        }
        return false;
    }
    public static InputStream inputStream(File _file) {
        Filer filer = null;
        try {
            filer = Filer.open(_file, IFiler.cRead);
            return new InputStreamFiler(filer);
        } catch (IOException e) {
            try {
                filer.close();
            } catch (Exception xx) {
            }
            return null;//!! failed to move

        }
    }
    public static Filer inputFiler(File _file) {
        Filer filer = null;
        try {
            filer = Filer.open(_file, IFiler.cRead);
            return filer;
        } catch (IOException e) {
            try {
                filer.close();
            } catch (Exception xx) {
            }
            return null;//!! failed to move

        }
    }
    public static File[] fsRoots() {
        return File.listRoots();
    }
    public static File find(IOut _, String _name) {
        _name = _name.toLowerCase();
        File found = null;
        File[] roots = fsRoots();
        for (int r = 0; r < roots.length; r++) {
            if (_.canceled()) {
                break;
            }
            _.out(roots[r].toString());
            found = _find(_, roots[r], _name.toLowerCase());
            if (found != null) {
                break;
            }
        }
        return found;
    }
    private static File _find(IOut _, File _directory, String _name) {
        File[] array = _directory.listFiles();
        _.out(_directory.toString());
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (_.canceled()) {
                    break;
                }
                _.out(i, array.length);
                File _file = array[i];
                if (_file.isDirectory()) {
                    _find(_, _file, _name);
                } else if (_file.getName().toLowerCase().equals(_name)) {
                    return _file;
                }
            }
        }
        return null;
    }
    public static File nextAvailableFile(File _folder, String _name) throws IOException {
        File file = new File(_folder, _name);
        for (int i = 1; file.exists(); file = new File(_folder, i + _name), i++);
        file.createNewFile();
        return file;
    }
    public static File nextAvailableFolder(File _folder, String _name) throws IOException {
        File folder = new File(_folder, _name);
        for (int i = 1; folder.isDirectory(); folder = new File(_folder, i + _name), i++);
        folder.mkdir();
        return folder;

    }
    public static boolean copyTo(File _from, File _to) throws Exception {
        boolean fromIsDir = _from.isDirectory();
        boolean toIsDir = _to.isDirectory();

        if (fromIsDir != toIsDir) {
            throw new Exception(_from + " isn't the same type as " + _to);
        }
        if (_from.isDirectory()) {
            File[] array = _from.listFiles();
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    File copyTo = new File(_to, array[i].getName());
                    if (array[i].isDirectory()) {
                        copyTo.mkdir();
                    }
                    copyTo(array[i], copyTo);//!!recursion

                }
            }
        } else {
            if (_to.exists()) {
                return true;// replace or skip
            }
            File parent = _to.getParentFile();
            if (parent != null) {
                parent.mkdirs();
                //_to.createNewFile();
            }
            InputStreamFiler from = new InputStreamFiler(Filer.open(_from, IFiler.cRead));
            OutputStreamFiler to = new OutputStreamFiler(Filer.open(_to, IFiler.cReadWrite));
            to.eof();
            BufferedInputStream f = new BufferedInputStream(from, 16384);
            BufferedOutputStream t = new BufferedOutputStream(to, 16384);

            int i = -1;
            while ((i = f.read()) != -1) {
                t.write(i);
            }
            t.flush(); //dg

            from.close();
            to.close();
        }
        return true;
    }
    public static boolean replaceTo(File _from, File _to) throws Exception {
        boolean fromIsDir = _from.isDirectory();
        boolean toIsDir = _to.isDirectory();

        if (fromIsDir != toIsDir) {
            throw new Exception(_from + " isn't the same type as " + _to);
        }
        if (_from.isDirectory()) {
            File[] array = _from.listFiles();
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    File copyTo = new File(_to, array[i].getName());
                    if (array[i].isDirectory()) {
                        copyTo.mkdir();
                    }
                    copyTo(array[i], copyTo);//!!recursion

                }
            }
        } else {
            File parent = _to.getParentFile();
            if (parent != null) {
                parent.mkdirs();
                //_to.createNewFile();
            }
            InputStreamFiler from = new InputStreamFiler(Filer.open(_from, IFiler.cRead));
            OutputStreamFiler to = new OutputStreamFiler(Filer.open(_to, IFiler.cReadWrite));
            to.eof();
            BufferedInputStream f = new BufferedInputStream(from, 16384);
            BufferedOutputStream t = new BufferedOutputStream(to, 16384);

            int i = -1;
            while ((i = f.read()) != -1) {
                t.write(i);
            }
            t.flush(); //dg

            from.close();
            to.close();
        }
        return true;
    }
    // Expects IFiler to already be buffered
    public static void copyTo(IOut _, File _file, IFiler _to) throws Exception {
        long l = _file.length();
        UIO.writeLong(_to, l, "Length");
        if (l > 0) {
            InputStreamFiler fs = new InputStreamFiler(Filer.open(_file, IFiler.cRead));
            BufferedInputStream _from = new BufferedInputStream(fs, 16384);
            for (long i = 0; i < l; i++) {
                if (i % 1024 == 0) {
                    _.out(i, l);
                }
                _to.write(_from.read());
            }
            _from.close();
        }
    }
    // Expects IFiler to already be buffered
    public static void copyTo(IOut _, IFiler _from, File _file) throws Exception {
        File parent = _file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        long l = UIO.readLong(_from, "Length");
        OutputStreamFiler of = new OutputStreamFiler(Filer.open(_file, IFiler.cReadWrite));
        of.eof();
        if (l > 0) {
            BufferedOutputStream _to = new BufferedOutputStream(of, 16384);
            for (long i = 0; i < l; i++) {
                if (i % 1024 == 0) {
                    _.out(i, l);
                }
                _to.write(_from.read());
            }
            _to.close();
        }
    }
    public static int equal(IOut _, File _from, File _to) {
        boolean fromIsDir = _from.isDirectory();
        boolean toIsDir = _to.isDirectory();
        if (fromIsDir || toIsDir) {
            return _from.equals(_to) ? 1 : 0;
        }
        if (!_from.exists()) {
            return 0;
        }
        if (!_to.exists()) {
            return 0;
        }
        _.out(" Testing " + _from.getName() + " against " + _to.getName());
        try {
            InputStreamFiler from = new InputStreamFiler(Filer.open(_from, IFiler.cRead));
            InputStreamFiler to = new InputStreamFiler(Filer.open(_to, IFiler.cReadWrite));
            BufferedInputStream f = new BufferedInputStream(from, 16384);
            BufferedInputStream t = new BufferedInputStream(to, 16384);
            long l = _from.length();
            int run = 0;
            int fr = -1;
            int tr = -1;
            while (((fr = f.read()) != -1) && ((tr = t.read()) != -1)) {
                if (fr != tr) {
                    return run;
                }
                run++;
                if (run % 1024 == 0) {
                    _.out(run, (int) l);
                }
            }
            from.close();
            to.close();
            return (fr == tr) ? run : 0;
        } catch (Exception x) {
            _.out(x);
            x.printStackTrace();
            return 0;
        }
    }
    public static Exception emptyDirectory(File _file) {
        try {
            if (!_file.isDirectory()) {
                return null;
            }
            File[] array = _file.listFiles();
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    remove(array[i]);
                }
            }
            return null;
        } catch (Exception x) {
            return x;
        }
    }
    public static Exception ensureDirectory(File _file) {
        if (_file == null) {
            return null;
        }
        try {
            if (_file.exists()) {
                return null;
            }
            File parent = _file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            return null;
        } catch (Exception x) {
            return x;
        }
    }
    public static boolean directoryExistsFor(File _file) {
        if (_file == null) {
            return false;
        }
        try {
            File parent = _file.getParentFile();
            if (parent.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception x) {
            return false;
        }
    }
    public static Exception extract(File _oldDir, File _newDir) {
        // flattens, renames, moves; why? was needed to parse slow truth files.
        // get all files from all _oldDir directories, rename "1001" ... "nnnn"
        // and relocate under _newDir, which should exist before extract  
        try {
            File[] dirs = UFile.allFiles(new SysOut(), _oldDir);
            int id = 1000;
            for (int i = 0; i < dirs.length; i++) {
                id++;
                File newFile = new File(_newDir, "" + id);
                UFile.moveTo(dirs[i], newFile);
            }
            return null;
        } catch (Exception x) {
            return x;
        }
    }
    public static Exception moveTo(File _from, File _to) {
        try {
            _from.renameTo(_to);
            return null;
        } catch (Exception x) {
            return x;
        }
    }
    public static Exception remove(File _remove) {
        try {
            if (_remove.isDirectory()) {
                File[] array = _remove.listFiles();
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        remove(array[i]);
                    }
                }
            }
            _remove.delete();
            return null;
        } catch (Exception x) {
            return x;
        }
    }
    public static void allFiles(IOut _, File _file, ICallback<File,File> _callback) {
        if (_file == null) {
            return;
        }
        if (_file.isDirectory()) {
            _allFiles(_, _file, _callback);
        } else {
            _callback.callback(_file);
        }
    }
    private static void _allFiles(IOut _, File _directory, ICallback<File,File> _callback) {
        File[] array = _directory.listFiles();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (_.canceled()) {
                    break;
                }
                _.out(i, array.length);
                File _file = array[i];
                _.out(_file);
                if (_file.isDirectory()) {
                    //_.pushProgress();
                    _allFiles(_, _file, _callback);
                    //_.popProgress();
                } else {
                    _callback.callback(_file);
                }
            }
        }
    }
    public static File[] allFiles(IOut _, File _file) {
        if (_file == null) {
            return null;
        }
        if (_file.isDirectory()) {
            CArray all = new CArray(File.class);
            _allFiles(_, _file, all);
            return (File[]) all.getAll();
        } else {
            return new File[]{_file};
        }
    }
    private static void _allFiles(IOut _, File _directory, CArray _all) {
        File[] array = _directory.listFiles();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (_.canceled()) {
                    break;
                }
                _.out(i, array.length);
                File _file = array[i];
                _.out(_file);
                if (_file.isDirectory()) {
                    //_.pushProgress();
                    _allFiles(_, _file, _all);
                    //_.popProgress();
                } else {
                    _all.insertLast(_file);
                }
            }
        }
    }
    public static String[] toStrings(File[] _files) {
        CArray strings = new CArray(String.class);
        for (int i = 0; i < _files.length; i++) {
            if (_files[i] == null) {
                continue;
            }
            strings.insertLast(_files[i].getAbsolutePath());
        }
        return (String[]) strings.getAll();
    }
    public static File home() {
        return new File(System.getProperty("user.dir"));
    }
    public static String getPathRelativeToHome(File _file) {
        return getRelativePath(new File(System.getProperty("user.dir")), _file);
    }
    public static String getRelativePath(File _root, File _file) {
        String home = _root.getAbsolutePath();
        String path = _file.getAbsolutePath();
        if (!path.startsWith(home)) {
            return null;
        }
        String relative = path.substring(home.length() + 1);//BUG Fix add +1 on 7-16-08

        return relative;
    }
    public static String getName(String _name) {
        int p = -1;
        if ((p = _name.lastIndexOf('.')) < 0) {
            return _name;
        }
        return (_name.substring(0, p)).toLowerCase();
    }
    public static String getExtension(String _name) {
        int p = -1;
        if ((p = _name.lastIndexOf('.')) < 0) {
            return "";
        }
        return (_name.substring(p + 1)).toLowerCase();
    }
    public static String replaceExtension(String _name, String _ext) {
        int p = -1;
        if ((p = _name.lastIndexOf('.')) < 0) {
            return _name + "." + _ext;
        }
        return _name.substring(0, p + 1) + _ext;
    }
    public static String removeExtension(String _name) {
        int p = -1;
        if ((p = _name.lastIndexOf('.')) < 0) {
            return _name;
        }
        return _name.substring(0, p);
    }
    public static String removePath(String _name) {
        int p = -1;
        if ((p = _name.lastIndexOf(File.separatorChar)) < 0) {
            return _name;
        }
        return (_name.substring(p + 1));
    }
    public static String getPath(String _name) {
        return getPath(_name, File.separatorChar);
    }
    public static String getPath(String _name, char _char) {
        int p = -1;
        if ((p = _name.lastIndexOf(_char)) < 0) {
            return "";
        }
        return (_name.substring(0, p + 1));
    }
}
