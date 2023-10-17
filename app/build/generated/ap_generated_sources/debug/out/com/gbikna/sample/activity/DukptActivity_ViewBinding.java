// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DukptActivity_ViewBinding implements Unbinder {
  private DukptActivity target;

  private View view7f0a01ab;

  private View view7f0a0142;

  private View view7f0a0140;

  private View view7f0a0141;

  private View view7f0a00d4;

  private View view7f0a01a8;

  private View view7f0a0178;

  @UiThread
  public DukptActivity_ViewBinding(DukptActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DukptActivity_ViewBinding(final DukptActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.inputPin, "method 'onClick'");
    view7f0a01ab = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dukptInit, "method 'onClick'");
    view7f0a0142 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dukptDeccrypt, "method 'onClick'");
    view7f0a0140 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dukptEncrypt, "method 'onClick'");
    view7f0a0141 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.calcMac, "method 'onClick'");
    view7f0a00d4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.increaseKsn, "method 'onClick'");
    view7f0a01a8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.getKsn, "method 'onClick'");
    view7f0a0178 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    DukptActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;

    view7f0a01ab.setOnClickListener(null);
    view7f0a01ab = null;
    view7f0a0142.setOnClickListener(null);
    view7f0a0142 = null;
    view7f0a0140.setOnClickListener(null);
    view7f0a0140 = null;
    view7f0a0141.setOnClickListener(null);
    view7f0a0141 = null;
    view7f0a00d4.setOnClickListener(null);
    view7f0a00d4 = null;
    view7f0a01a8.setOnClickListener(null);
    view7f0a01a8 = null;
    view7f0a0178.setOnClickListener(null);
    view7f0a0178 = null;
  }
}
