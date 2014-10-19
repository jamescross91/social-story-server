package com.colibri.social_story.transport;

import com.colibri.social_story.FirebaseValueEventListenerAdapter;
import com.colibri.social_story.entities.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class FBUserPersister implements UserStore {

    private static final Logger log = Logger.getLogger(FBUserPersister.class.getName());

    private static final String USERS = "users/";
    private final Firebase fb;
    private final Vector<User> userCache = new Vector<>();

    public FBUserPersister(Firebase fb) {
        this.fb = fb;
    }

    @Override
    public void persistUser(User u) {
        CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS + u.getUserName()).setValue(
                (Object) u, new ReleaseLatchCompletionListener(done));
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** A vector to update with the user information */
    @Override
    public void syncLoadUsers(final Vector<User> users) {
        log.info("Loading all users");
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS).addValueEventListener(new FirebaseValueEventListenerAdapter() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    users.add((User) ds.getValue());
                    done.countDown();
                }
            }
        });
        try {
            done.await();
            log.info("Finished loading users");
            log.info(userCache.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByID(UserID userID) {
        for (User u : userCache) {
            if (u.getUid().equals(userID))
                return u;
        }
        return null;
    }
}
