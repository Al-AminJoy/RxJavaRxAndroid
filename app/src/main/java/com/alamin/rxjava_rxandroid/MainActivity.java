package com.alamin.rxjava_rxandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alamin.rxjava_rxandroid.pojo.Task;
import com.alamin.rxjava_rxandroid.service.DataSource;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private SeekBar seekBar;
    private String TAG="main_activity";
    private CompositeDisposable disposables=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //textView=findViewById(R.id.textView);
        //seekBar=findViewById(R.id.seekBar);
        //observable();
        //Operators

        //Create
        //createOperator();
       // rangeOperator();
       // repeatOperator();
        //intervalAndTimerOperator();

        //Distinct
        //distinctOperator();
        //simpleBufferOperator();
        advancedBufferOperator();
    }

    private void advancedBufferOperator() {
        /**
         * Counts How Many Clicks In 4 Second
         * Actually This Is Creating a List and We Are Checking The Size of That List
         */
        RxView.clicks(findViewById(R.id.button))
                .map(new Function<Unit, Object>() {
                    @Override
                    public Object apply(@androidx.annotation.NonNull Unit unit) throws Exception {
                        return 1;
                    }
                })
                .buffer(4,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {
                        disposables.add(disposable);
                    }

                    @Override
                    public void onNext(@androidx.annotation.NonNull List<Object> objects) {
                        Log.d(TAG, "onNext: You clicked " + objects.size() + " times in 4 seconds!");

                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void simpleBufferOperator() {
        /**
         * Creates Bundle of Object
         */
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io());
        taskObservable
        .buffer(2)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Task>>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull List<Task> tasks) {
                Log.d(TAG, "onNext: bundle results: -------------------");
                for(Task task: tasks){
                    Log.d(TAG, "onNext: " + task.getDescription());
                }
            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
     ;
    }

    private void distinctOperator() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .distinct(new Function<Task, String>() {
                    @Override
                    public String apply(@androidx.annotation.NonNull Task task) throws Exception {
                        return task.getDescription();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull Task task) {
                Log.d(TAG, "onNext: "+ task.getDescription());

            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void intervalAndTimerOperator() {

        //For Interval
      /*  Observable<Long> longObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(@androidx.annotation.NonNull Long aLong) throws Exception {
                        return aLong<=5;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
*/
        Observable<Long> longObservable = Observable
                .timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        longObservable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull Long aLong) {
                Log.d(TAG, "onNext: "+ aLong);

            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void repeatOperator() {
        Observable<Integer> taskObservable = Observable
                .range(0,3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeat(3);

        taskObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull Integer integer) {
                Log.d(TAG, "onNext: "+ integer);

            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void rangeOperator() {
        Observable<Task> taskObservable = Observable
                .range(0,9)
                .map(new Function<Integer, Task>() {
                    @Override
                    public Task apply(@androidx.annotation.NonNull Integer integer) throws Exception {
                        return new Task("Task By Map With Priority "+String.valueOf(integer),true,integer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull Task task) {
                Log.d(TAG, "onNext: "+ task.getDescription());

            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void createOperator() {
       //Task task = new Task("Take out the trash", true, 3);

        List<Task> tasks = DataSource.createTasksList();

        Observable<Task> taskObservable = Observable
                .create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(@androidx.annotation.NonNull ObservableEmitter<Task> emitter) throws Exception {
                     //For Single Object
                      /*  if (!emitter.isDisposed()){
                            emitter.onNext(task);
                            emitter.onComplete();
                        }*/

                        //For List Of Objects
                        for (Task task : tasks){
                            if (!emitter.isDisposed()){
                                emitter.onNext(task);
                            }
                        }
                        if (!emitter.isDisposed()){
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@androidx.annotation.NonNull Task task) {
                Log.d(TAG, "onNext: "+ task.getDescription());
            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observable() {
        Observable<Task> taskObservable=Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(@NonNull Task task) throws Exception {
                        Thread.sleep(1000);
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());


        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe Called ");
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: : " + task.getDescription());

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete Called ");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}