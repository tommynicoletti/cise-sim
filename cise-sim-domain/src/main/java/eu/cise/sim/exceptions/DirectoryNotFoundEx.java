package eu.cise.sim.exceptions;

import java.io.IOException;

//wrapper of io exception like java.nio.file.NoSuchFileException
public class DirectoryNotFoundEx extends LoaderEx {

    public DirectoryNotFoundEx(IOException e) {
        super("as directory is not found", e);
    }
}