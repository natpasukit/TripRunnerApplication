package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;

import uk.ac.shef.oak.com6510.Views.GalleryActivity;

public class GalleryViewModel {
    Application application = null;
    Context context = null;

    public GalleryViewModel(Application application, Context context) {
        super();
        this.application = application;
        this.context = context;
    }
}
