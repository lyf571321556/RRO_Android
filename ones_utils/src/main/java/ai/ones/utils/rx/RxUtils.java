package ai.ones.utils.rx;


import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2018/10/24.
 * 因为目前ones-project-android中使用的事rx1的版本，因此后续升级才能使用
 */
public class RxUtils {

    /**
     * rxjava create操作符
     *
     * @param observable observable
     * @param consumer consumer
     * @param long delay
     */
//    public static <T> void rxCreateOperation(ObservableOnSubscribe<T> observable, Consumer<T> consumer,long delay) {
//        Observable.create(observable)
//                .delay(delay,TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(consumer);
//    }

    /**
     * rxjava create操作符
     *
     * @param observable observable
     * @param consumer consumer
     * @param <T> <T>
     */
//    public static <T> void rxCreateOperation(ObservableOnSubscribe<T> observable, Consumer<T> consumer) {
//        Observable.create(observable)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(consumer);
//    }

    /**
     * create 不需要回调
     *
     * @param observable observable
     * @param <T> <T>
     */
//    public static <T> void rxCreateOperationNoCallBack(ObservableOnSubscribe<T> observable) {
//        Observable.create(observable)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
//    }

    /**
     * rxjava create操作符 延时调用
     * @param observable observable
     * @param consumer consumer
     * @param delay 延时时间
     * @param <T> <T>
     * @return Disposable
     */
//    public static <T> Disposable rxCreateOperationDelay(ObservableOnSubscribe<T> observable, Consumer<T> consumer, long delay) {
//        return Observable.create(observable)
//                .subscribeOn(Schedulers.newThread())
//                .delay(delay, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(consumer);
//    }

}
