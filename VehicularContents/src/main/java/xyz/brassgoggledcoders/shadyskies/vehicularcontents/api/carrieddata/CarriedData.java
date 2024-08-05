package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.ObjectUtils;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class CarriedData {
    private static final Object2IntMap<Class<? extends ICarriedContent>> CONTENT_ID_POOL = new Object2IntOpenHashMap<>();

    private final Int2ObjectMap<CarriedDataItem<?>> itemsById;
    private final ReadWriteLock lock;
    private boolean needsSync;

    public CarriedData() {
        this.itemsById = new Int2ObjectOpenHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public <T> void define(CarriedDataAccessor<T> accessor, T value) {
        this.define(accessor, value, false, true);
    }

    public <T> void define(CarriedDataAccessor<T> accessor, T value, boolean sync, boolean save) {
        int id = accessor.id();
        if (this.itemsById.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        } else {
            this.createDataItem(accessor, value, sync, save);
        }
    }

    private <T> void createDataItem(CarriedDataAccessor<T> accessor, T value, boolean sync, boolean save) {
        CarriedDataItem<T> dataItem = new CarriedDataItem<>(accessor, value, save, sync);
        this.lock.writeLock().lock();
        this.itemsById.put(accessor.id(), dataItem);
        this.lock.writeLock().unlock();
    }

    public <T> boolean hasItem(CarriedDataAccessor<T> dataAccessor) {
        return this.itemsById.containsKey(dataAccessor.id());
    }

    @SuppressWarnings("unchecked")
    private <T> CarriedDataItem<T> getItem(CarriedDataAccessor<T> dataAccessor) {
        this.lock.readLock().lock();

        CarriedDataItem<T> dataItem;
        try {
            dataItem = (CarriedDataItem<T>) this.itemsById.get(dataAccessor.id());
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.forThrowable(throwable, "Getting carried data");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Carried data");
            crashReportCategory.setDetail("Data ID", dataAccessor);
            throw new ReportedException(crashReport);
        } finally {
            this.lock.readLock().unlock();
        }

        return dataItem;
    }

    public <T> T get(CarriedDataAccessor<T> dataAccessor) {
        return this.getItem(dataAccessor).getValue();
    }

    public <T> void set(CarriedDataAccessor<T> dataAccessor, T value) {
        this.set(dataAccessor, value, false);
    }

    public <T> void set(CarriedDataAccessor<T> dataAccessor, T value, boolean forceUpdate) {
        CarriedDataItem<T> dataItem = this.getItem(dataAccessor);
        if (forceUpdate || ObjectUtils.notEqual(value, dataItem.getValue())) {
            dataItem.setValue(value);
            onDataUpdated(dataItem);
            dataItem.setDirty(true);
            if (dataItem.shouldSync()) {
                this.needsSync = true;
            }
        }
    }

    public boolean needsSync() {
        return this.needsSync;
    }

    protected void onDataUpdated(CarriedDataItem<?> dataItem) {

    }

    protected void onDataUpdated(List<CarriedDataValue<?>> dataValues) {

    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (CarriedDataItem<?> dataItem : this.itemsById.values()) {
            if (dataItem.shouldSave() && dataItem.isNotDefault()) {
                nbt.put(
                        dataItem.getAccessor()
                                .name(),
                        dataItem.asTag()
                );
            }
        }

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        for (CarriedDataItem<?> dataItem : this.itemsById.values()) {
            if (dataItem.shouldSave()) {
                String name = dataItem.getAccessor()
                        .name();
                if (nbt.contains(name)) {
                    dataItem.fromTag(nbt.get(name));
                }
            }
        }
    }

    @Nullable
    public List<CarriedDataValue<?>> packDirty() {
        List<CarriedDataValue<?>> dataValues = null;
        if (this.needsSync()) {
            this.lock.readLock().lock();

            for (CarriedDataItem<?> dataItem : this.itemsById.values()) {
                if (dataItem.isDirty()) {
                    dataItem.setDirty(false);
                    if (dataValues == null) {
                        dataValues = new ArrayList<>();
                    }

                    dataValues.add(dataItem.asDataValue());
                }
            }

            this.lock.readLock().unlock();
        }

        this.needsSync = false;
        return dataValues;
    }

    @Nullable
    public List<CarriedDataValue<?>> getNonDefaultValues() {
        List<CarriedDataValue<?>> dataValues = null;
        this.lock.readLock().lock();

        for (CarriedDataItem<?> dataItem : this.itemsById.values()) {
            if (dataItem.isNotDefault() && dataItem.shouldSave()) {
                if (dataValues == null) {
                    dataValues = new ArrayList<>();
                }

                dataValues.add(dataItem.asDataValue());
            }
        }

        this.lock.readLock().unlock();
        return dataValues;
    }

    public void assignValues(List<CarriedDataValue<?>> dataValues) {
        this.lock.writeLock().lock();

        try {
            for (CarriedDataValue<?> dataValue : dataValues) {
                CarriedDataItem<?> dataItem = this.itemsById.get(dataValue.id());
                if (dataItem != null) {
                    this.assignValue(dataItem, dataValue);
                    this.onDataUpdated(dataItem);
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }

        this.onDataUpdated(dataValues);
    }

    @SuppressWarnings("unchecked")
    private <T> void assignValue(CarriedDataItem<T> dataItem, CarriedDataValue<?> dataValue) {
        if (!Objects.equals(dataValue.serializer(), dataItem.getAccessor().serializer())) {
            throw new IllegalStateException(
                    String.format(
                            Locale.ROOT,
                            "Invalid carried data item type for field %d on carrier %s: old=%s(%s), new=%s(%s)",
                            dataItem.getAccessor().id(),
                            this.getCarrier(),
                            dataItem.getValue(),
                            dataItem.getValue().getClass(),
                            dataValue.value(),
                            dataValue.value().getClass()
                    )
            );
        } else {
            dataItem.setValue((T) dataValue.value());
        }
    }

    protected abstract IContentCarrier getCarrier();

    public static <T> CarriedDataAccessor<T> definedId(
            Class<? extends ICarriedContent> clazz,
            String name,
            ICarriedDataSerializer<T> serializer
    ) {
        int id;
        if (CONTENT_ID_POOL.containsKey(clazz)) {
            id = CONTENT_ID_POOL.getInt(clazz) + 1;
        } else {
            int parentIdCheck = 0;
            Class<?> classCheck = clazz;

            while (classCheck != Entity.class) {
                classCheck = classCheck.getSuperclass();
                if (CONTENT_ID_POOL.containsKey(classCheck)) {
                    parentIdCheck = CONTENT_ID_POOL.getInt(classCheck) + 1;
                    break;
                }
            }

            id = parentIdCheck;
        }

        CONTENT_ID_POOL.put(clazz, id);
        return new CarriedDataAccessor<>(
                id,
                name,
                serializer
        );
    }
}
