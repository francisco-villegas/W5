package com.example.pancho.w5.view.settingsactivity;

import com.example.pancho.w5.view.settingsactivity.SettingsActivityContract;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public class SettingsActivityPresenter implements SettingsActivityContract.Presenter {
    SettingsActivityContract.View view;
    private static final String TAG = "SettingsActivityPresenter";

    @Override
    public void attachView(SettingsActivityContract.View view) {
        this.view = view;
    }
    @Override
    public void detachView() {
        this.view = null;
    }

//    // TODO: 22/08/2017 Implement firebase logic to save data
//    @Override
//    public void saveMessageToCloud(String s) {
//
//        // Write a message to the database
//        messageReference = database.getReference("message");
//
//        messageReference.setValue("Hello, World!");
//
//        //if save to cloud
//        view.onDataSaved(true);
//    }

//    public void getMessageCloud() {
//        // Read from the database
//        messageReference = database.getReference("message");
//        messageReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//                //view.sendData(value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });




//    }

//    @Override
//    public void pushMovieToDB(Movie movie) {
//        DatabaseReference movieReference = database.getReference("movies");
//
//        //saving using default key method
//        movieReference.push().setValue(movie);
//
//        //making the key as movie.getName()
//        movieReference.child(movie.getName()).setValue(movie);
//
//        //adding multiple objects
//        for (int i = 0; i < 5; i++) {
//            movieReference.child("Movie" + i).setValue(movie);
//        }
//    }
//
//    @Override
//    public void getMovieCloud() {
//        movieReference = database.getReference("movies");
//        movieReference.child("Movie0").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Movie movie = dataSnapshot.getValue(Movie.class);
//                String value = movie.getName() + " " + movie.getDirector() + " " + movie.getYear();
//                view.sendData(value);
//                Log.d(TAG, "Movie is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        final List<Movie> movieList = new ArrayList<>();

//        movieReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//
//                    Movie movie =  snapshot.getValue(Movie.class);
//                    movieList.add(movie);
//
//
//                }
//                view.updateMovieList(movieList);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//    }
}
