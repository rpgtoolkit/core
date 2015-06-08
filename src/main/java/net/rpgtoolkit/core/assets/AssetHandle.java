/*
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public interface AssetHandle {

    public AssetDescriptor getDescriptor();
    
    public Asset getAsset();
    
    public void setAsset(Asset asset);
        
    public InputStream getInputStream() throws IOException;
    
    public OutputStream getOutputStream() throws IOException;
    

}
