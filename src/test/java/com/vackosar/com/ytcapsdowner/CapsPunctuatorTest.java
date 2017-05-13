package com.vackosar.com.ytcapsdowner;

import android.os.AsyncTask;
import android.widget.TextView;

import com.vackosar.ytcapsdowner.CapsDownloader;
import com.vackosar.ytcapsdowner.CapsPunctuator;
import com.vackosar.ytcapsdowner.Punctuator;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CapsPunctuatorTest {

    @Test
    public void skipIfPunctuated() throws ExecutionException, InterruptedException {
        CapsDownloader downloader = mock(CapsDownloader.class);
        AsyncTask task = mock(AsyncTask.class);
        when(task.get()).thenReturn("Test.");
        when(downloader.execute(anyString())).thenReturn(task);
        CapsPunctuator capsPunctuator = new CapsPunctuator(mock(TextView.class), downloader, mock(Punctuator.class));
        capsPunctuator.punctuate("testurl");
    }
}
