package com.example.tmaslon.testapp.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.tmaslon.testapp.data.JobsContract;

/**
 * Created by tmaslon on 3/24/2017.
 */
public class AccountUtils {

    public static final String ACCOUNT_NAME = "Account";

    /**
     * It is important that the accountType specified here matches the value in your sync adapter
     * configuration XML file for android.accounts.AccountAuthenticator (often saved in
     * {@code res/xml/syncadapter.xml}). If this is not set correctly, you'll receive an error indicating
     * that "caller uid XXXXX is different than the authenticator's uid".
     */
    public static final String ACCOUNT_TYPE = "com.example.tmaslon.testapp.account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";

    /**
     * Obtain a handle to the {@link android.accounts.Account} used for sync in this application.
     *
     * @return Handle to application's account (not guaranteed to resolve unless createSyncAccount()
     * has been called)
     */
    @NonNull
    public static Account getAccount() {
        // Note: Normally the account name is set to the user's identity (username or email
        // address). However, since we aren't actually using any user accounts, it makes more sense
        // to use a generic string in this case.
        //
        // This string should *not* be localized. If the user switches locale, we would not be
        // able to locate the old account, and may erroneously register multiple accounts.
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    public static boolean addAccount(Context context, Account account, long pollFrequency) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, JobsContract.CONTENT_AUTHORITY, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, JobsContract.CONTENT_AUTHORITY, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account, JobsContract.CONTENT_AUTHORITY, new Bundle(), pollFrequency);
            return true;
        }
        return false;
    }

}
