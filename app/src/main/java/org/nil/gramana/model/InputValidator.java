package org.nil.gramana.model;

/**
 * Created by n0ne on 09/10/16.
 */
public interface InputValidator <T> {

    public boolean isInputValid (T data);

    /**
     *
     * @return an user readable message for why the input is invalid
     */
    public String getErrorMessage ();

}
