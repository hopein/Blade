package blade.mvp;

/**
 * Interface {@link IPresenter}.
 * Class that implements this interface has to have default constructor.
 *
 * @param <V> View type
 * @param <D> Data type
 */
public interface IPresenter<V extends IView, D> {

    /**
     * Called after this presenter has been created via default constructor.
     *
     * @param data        Data for initialization
     * @param wasRestored Flag signalling whether this Presenter was newly created, or restored from state.
     */
    void create(D data, boolean wasRestored);

    /**
     * Called before destroying this Presenter.
     */
    void destroy();

    /**
     * Called every time this Presenter is connecting to a view.
     *
     * @param view View
     */
    void bind(V view);

    /**
     * Called every time this Presenter is disconnecting from a view.
     */
    void unbind();

    /**
     * Saves state to given state object.
     * <p/>
     * Normally this would be an instance of android's Bundle class, but in order to allow unit testing there is just Object as parameter type.
     */
    void saveState(Object state);

    /**
     * Restores state from given state object.
     * <p/>
     * Normally this would be an instance of android's Bundle class, but in order to allow unit testing there is just Object as parameter type.
     */
    void restoreState(Object state);

}
