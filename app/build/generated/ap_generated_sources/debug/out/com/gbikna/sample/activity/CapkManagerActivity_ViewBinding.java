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

public class CapkManagerActivity_ViewBinding implements Unbinder {
  private CapkManagerActivity target;

  private View view7f0a012f;

  private View view7f0a00f5;

  private View view7f0a017a;

  @UiThread
  public CapkManagerActivity_ViewBinding(CapkManagerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CapkManagerActivity_ViewBinding(final CapkManagerActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.downloadPuk, "method 'onClick'");
    view7f0a012f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.clearPuk, "method 'onClick'");
    view7f0a00f5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.getPukList, "method 'onClick'");
    view7f0a017a = view;
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
    CapkManagerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;

    view7f0a012f.setOnClickListener(null);
    view7f0a012f = null;
    view7f0a00f5.setOnClickListener(null);
    view7f0a00f5 = null;
    view7f0a017a.setOnClickListener(null);
    view7f0a017a = null;
  }
}
