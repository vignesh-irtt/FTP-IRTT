package com.vignesh.study;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {
    Exception e;
    FileArray adapter = null;
    FTPClient ftpClient = null;
    List<File> dir;
    List<File> fls;
    ListView mListView;
    FTPFile[] files;
    String src = "/";
    View view;
    String backsrc;
    ProgressDialog progDailog;
    NotificationManager mng = null;
    NotificationCompat.Builder build[] = new NotificationCompat.Builder[1000];
    Intent openfile=null;
    java.io.File fileop=null;
    PendingIntent popenfile=null;
    int notno=0;
    public ItemFragment() {
        dir = new ArrayList<>();
        fls = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        openfile=new Intent();
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mng = (NotificationManager) getActivity().getSystemService(getActivity().getApplicationContext().NOTIFICATION_SERVICE);
        return view;
    }

    public class connect extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progDailog = new ProgressDialog(getActivity());
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Connecting", Toast.LENGTH_SHORT).show();
                    }
                });

                String server = "210.212.247.212";
                int port = 21;
                String user = "anonymous";
                String pass = "password@gmail.com";
                ftpClient = new FTPClient();
                ftpClient.connect(server, port);
                ftpClient.enterLocalPassiveMode();
                ftpClient.login(user, pass);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                int replyCode = ftpClient.getReplyCode();
                if (FTPReply.isPositiveCompletion(replyCode))
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                        }
                    });
                new ListFiles().execute("");
            } catch (Exception e1) {
                e = e1;
                getActivity().runOnUiThread(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Network UnAvailable", Toast.LENGTH_SHORT).show();

                                progDailog.hide();
                            }
                        });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }
    }

    public class ListFiles extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            progDailog.setTitle("Loading");
            progDailog.show();
            dir.removeAll(dir);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            //dir.add(new File("cgb", "cfbc", "fb", "cfg", "directory_icon"));
            src = urls[0];
            try {
                dir.clear();
                fls.clear();

                files = ftpClient.listFiles(src);
                for (FTPFile ff : files) {
                    DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormater.format(ff.getTimestamp().getTime());
                    if (ff.isDirectory()) {
                        FTPFile[] fbuf = ftpClient.listFiles(ff.getLink());
                        int buf;
                        if (fbuf != null) {
                            buf = fbuf.length;
                        } else buf = 0;
                        String num_item = String.valueOf(buf);
                        if (buf == 0) num_item = num_item + " item";
                        else num_item = num_item + " Items";

                        //String formated = lastModDate.toString();
                        dir.add(new File(ff.getName(), num_item, date, src +"/"+ ff.getName(), "directory_icon"));

                    } else {
                        fls.add(new File(ff.getName(), String.valueOf(ff.getSize()), date,src+"/" + ff.getName() , "file_icon"));
                    }
                }
            } catch (Exception e1) {
                e = e1;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), e.getMessage(), LENGTH_LONG).show();
                        progDailog.hide();
                    }
                });
               }
            if (!src.equals(""))
                dir.add(0, new File("Back", "", "", backsrc, "directory_up"));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    Collections.sort(dir);
                    Collections.sort(fls);
                    dir.addAll(fls);
                    adapter = new FileArray(getActivity(), R.layout.filelist, dir);
                    mListView.setAdapter(adapter);
                    progDailog.hide();
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        new connect().execute("");
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count=0;
        if (position == 0) {
           new ListFiles().execute(src.substring(0,src.lastIndexOf('/')));
            return;
            }
        File o = adapter.getItem(position);

        if (o.getImage().equalsIgnoreCase("directory_icon")) {

            Toast.makeText(getActivity(), o.getPath(), LENGTH_LONG).show();
            new ListFiles().execute(o.getPath());
        } else {
            Toast.makeText(getActivity(), o.getPath(), LENGTH_LONG).show();
            new FileDwn().execute(o.getPath(), o.getName(), o.getData());
        }
    }

    public class FileDwn extends AsyncTask<String, String, String> {
        InputStream inf = null;
        OutputStream outf = null;
        byte[] data = new byte[1024];
        long len, count = 0, total = 0;
        int notifyno=notno;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notno++;
            build[notifyno] = new NotificationCompat.Builder(getActivity().getBaseContext());
            build[notifyno].setContentTitle("Download");
            build[notifyno].setContentText("Download in Progress");
            build[notifyno].setSmallIcon(R.drawable.ic_down);
            build[notifyno].setAutoCancel(false);
            build[notifyno].setColor(Color.BLUE);
            build[notifyno].setOngoing(true);
            build[notifyno].setProgress(100, 0, false);
            mng.notify(notifyno, build[notifyno].build());
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                len = Integer.parseInt(params[2]);

                while(inf==null||!FTPReply.isPositiveCompletion(ftpClient.getReply()))
                    inf = ftpClient.retrieveFileStream(params[0]);
                outf = new FileOutputStream( "/sdcard/"+params[1]);
                fileop=new java.io.File("/sdcard/"+params[1]);
                while ((count = inf.read(data)) != -1) {
                    total += count;
                    publishProgress(String.valueOf((total * 100) / len));
                    outf.write(data,0,(int) count);
                }            } catch (final Exception e1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                             Toast.makeText(getActivity(),e1.getStackTrace().toString(),LENGTH_LONG).show();
 }
                });
                    }
            return null;
        }

        @Override
        protected void onProgressUpdate(final String... p) {
            build[notifyno].setProgress(100, Integer.parseInt(p[0]),true);
        }

        @Override
        protected void onPostExecute(String result) {
            build[notifyno].setContentText("Download Complete\nClick here to open");
            openfile.setAction(Intent.ACTION_VIEW);
            openfile.setDataAndType(Uri.fromFile(fileop), MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(fileop).toString())));
            build[notifyno].setProgress(0, 0, false);
            popenfile=PendingIntent.getActivity(getActivity(), 0, openfile, PendingIntent.FLAG_CANCEL_CURRENT);
            build[notifyno].setContentIntent(popenfile);
            build[notifyno].setOngoing(false);
            mng.notify(notifyno, build[notifyno].build());
        }
    }
}
