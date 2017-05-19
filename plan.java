//For saving file:
//Button triggers an action saving to the downloads folder
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

public void exportTransactions(String transactions) {
    File file = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS), "transactions.json");
    if (!file.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created");
    }
    FileOutputStream stream= new FileOutputStream(file);
    try{
        stream.write(transactiosn.getBytes());
    } finally {
        stream.close();
    }
}

//Import file:
//Could just try and import from known folder... try to find upload element. File Picker? Yeah we're doing that....

public void exportTransactions(String transactions) {
    File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "transactions.json");
    if (!file.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created");
    }
    StringBuilder text = new StringBuilder();
    try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();
    }
    catch (IOException e) {
	Log.e("A file read error occured",e.toString());
    }
    //Now try to JSON then save internally
}
