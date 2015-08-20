package empaticae4.hrker.com.empaticae4.Reports;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import empaticae4.hrker.com.empaticae4.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResponseActivityFragment extends Fragment {

    public ResponseActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_negative_response, container, false);
    }
}
