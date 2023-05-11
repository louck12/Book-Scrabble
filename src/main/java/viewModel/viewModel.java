package viewModel;

import model.Model;
import view.WindowController;

import java.util.Observable;
import java.util.Observer;

public class viewModel implements Observer {
    Model m;
    WindowController wc;

    public viewModel(Model m, WindowController wc){
        this.m = m;
        this.wc = wc;
        m.addObserver(this);
        wc.addObserver(this);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
