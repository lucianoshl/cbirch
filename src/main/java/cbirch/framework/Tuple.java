package cbirch.framework;


import cbirch.dataset.Image;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by void on 9/12/16.
 */
public class Tuple< T, T1 > {

    @Getter
    private final T first;

    @Getter
    @Setter
    private T1 second;


    public Tuple( T first, T1 second ) {
        this.first = first;
        this.second = second;
    }
}
