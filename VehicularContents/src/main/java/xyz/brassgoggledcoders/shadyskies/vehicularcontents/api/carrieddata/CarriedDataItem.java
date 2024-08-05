package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import net.minecraft.nbt.Tag;

public class CarriedDataItem<T> {
    private final CarriedDataAccessor<T> accessor;
    private final T defaultValue;
    private final boolean save;
    private final boolean sync;

    private T value;
    private boolean dirty;

    public CarriedDataItem(CarriedDataAccessor<T> accessor, T value, boolean save, boolean sync) {
        this.accessor = accessor;
        this.defaultValue = value;
        this.value = value;
        this.save = save;
        this.sync = sync;
    }

    public CarriedDataAccessor<T> getAccessor() {
        return accessor;
    }

    public boolean shouldSave() {
        return save;
    }

    public boolean shouldSync() {
        return sync;
    }

    public boolean isNotDefault() {
        return !this.defaultValue.equals(this.value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public CarriedDataValue<T> asDataValue() {
        return new CarriedDataValue<>(
                this.accessor.id(),
                this.accessor.serializer(),
                this.value
        );
    }

    public void fromTag(Tag tag) {
        this.setValue(this.getAccessor()
                .serializer()
                .read(tag)
        );
    }

    public Tag asTag() {
        return this.getAccessor()
                .serializer()
                .write(this.getValue());
    }
}
