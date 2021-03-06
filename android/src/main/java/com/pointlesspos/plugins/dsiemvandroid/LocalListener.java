package com.pointlesspos.plugins.dsiemvandroid;

import android.content.Context;
import android.util.Log;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fi.iki.elonen.NanoHTTPD;

//Local Listener class starts a post endpoint at this devices IP address on port 8080.
//Using the Datacap Client Test utility an integrator can test different transactions on the VP3300
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@CapacitorPlugin(name = "LocalListener")
//public class LocalListener<NanoHTTPD> extends NanoHTTPD {
public class LocalListener extends NanoHTTPD {
    private final Context AppContext;
    private static final Logger LOG = Logger.getLogger(LocalListener.class.getName());

    @PluginMethod()
    public LocalListener(Context appContext) throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        AppContext = appContext;
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        LocalListener.LOG.info(method + " '" + uri + "' ");
        Map<String, String> files = new HashMap<String, String>();
        if (method == Method.POST) {
            String returnMSG = "";
            try {
                session.parseBody(files);
                final String msg = files.get("postData");
                returnMSG = dsiEMVAndroidinstance.getInstance(AppContext).ProcessTransaction(msg);

            } catch (Exception ex) {
                Log.i("Local Listener", ex.getMessage());
            }


            return newFixedLengthResponse(returnMSG);
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}