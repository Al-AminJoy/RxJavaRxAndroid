package com.alamin.rxjava_rxandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alamin.rxjava_rxandroid.pojo.Task;
import com.alamin.rxjava_rxandroid.service.DataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private SeekBar seekBar;
    private String TAG="main_activity";
    private CompositeDisposable disposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView=findViewById(R.id.textView);
        seekBar=findViewById(R.id.seekBar);
        //observable();
        //Operators
        //createOperator();
       // rangeOperator();
        repeatOperator();
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
                disposable.add(d);
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
        disposable.clear();
    }
}