package com.thinksys.android.reachme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class ContactManager extends Activity
{

    public static final String TAG = "ContactManager";

    private ListView mContactList;
    private boolean mShowInvisible;
    private static Context context;

    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);
        ContactManager.context = getApplicationContext();
        mContactList = (ListView) findViewById(R.id.listView1);
        // Populate the contact list
        populateContactList();
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contactentry, cursor,
                fields, new int[] {R.id.contactEntryText});
        mContactList.setAdapter(adapter);
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getContacts()
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * Launches the ContactAdder activity to add a new contact to the selected accont.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(this, ContactAdder.class);
        startActivity(i);
    }
    
    public static Context getAppContext() {
        return ContactManager.context;
    }
}
