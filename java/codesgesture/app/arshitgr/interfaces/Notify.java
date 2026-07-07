package codesgesture.app.arshitgr.interfaces;

import codesgesture.app.arshitgr.Models.ProductModel;

public interface Notify {
    void onAdd(ProductModel data);
    void onRemove(ProductModel data);
}
