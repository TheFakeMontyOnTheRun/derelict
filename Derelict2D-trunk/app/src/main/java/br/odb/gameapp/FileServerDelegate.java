package br.odb.gameapp;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author monty
 */
public interface FileServerDelegate {

    InputStream openAsset(String filename) throws IOException;
}