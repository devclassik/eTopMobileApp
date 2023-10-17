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

public class AidManagerActivity_ViewBinding implements Unbinder {
  private AidManagerActivity target;

  private View view7f0a012e;

  private View view7f0a00f4;

  private View view7f0a0177;

  @UiThread
  public AidManagerActivity_ViewBinding(AidManagerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AidManagerActivity_ViewBinding(final AidManagerActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.downloadAid, "method 'onClick'");
    view7f0a012e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.clearAid, "method 'onClick'");
    view7f0a00f4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.getAidList, "method 'onClick'");
    view7f0a0177 = view;
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
    AidManagerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;

    view7f0a012e.setOnClickListener(null);
    view7f0a012e = null;
    view7f0a00f4.setOnClickListener(null);
    view7f0a00f4 = null;
    view7f0a0177.setOnClickListener(null);
    view7f0a0177 = null;
  }
}
