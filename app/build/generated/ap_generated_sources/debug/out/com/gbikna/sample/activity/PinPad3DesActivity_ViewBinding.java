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

public class PinPad3DesActivity_ViewBinding implements Unbinder {
  private PinPad3DesActivity target;

  private View view7f0a01e1;

  private View view7f0a01de;

  private View view7f0a01e2;

  private View view7f0a01ab;

  private View view7f0a00d4;

  private View view7f0a01af;

  @UiThread
  public PinPad3DesActivity_ViewBinding(PinPad3DesActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PinPad3DesActivity_ViewBinding(final PinPad3DesActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.loadmasterkey, "method 'onClick'");
    view7f0a01e1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.loadclearmasterkey, "method 'onClick'");
    view7f0a01de = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.loadworkkey, "method 'onClick'");
    view7f0a01e2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.inputPin, "method 'onClick'");
    view7f0a01ab = view;
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
    view = Utils.findRequiredView(source, R.id.inputoffPin, "method 'onClick'");
    view7f0a01af = view;
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
    PinPad3DesActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;

    view7f0a01e1.setOnClickListener(null);
    view7f0a01e1 = null;
    view7f0a01de.setOnClickListener(null);
    view7f0a01de = null;
    view7f0a01e2.setOnClickListener(null);
    view7f0a01e2 = null;
    view7f0a01ab.setOnClickListener(null);
    view7f0a01ab = null;
    view7f0a00d4.setOnClickListener(null);
    view7f0a00d4 = null;
    view7f0a01af.setOnClickListener(null);
    view7f0a01af = null;
  }
}
