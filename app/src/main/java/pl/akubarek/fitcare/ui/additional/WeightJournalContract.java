package pl.akubarek.fitcare.ui.additional;

import java.util.List;

import pl.akubarek.fitcare.model.Measurement;

/**
 * Created by BloodyFire on 03.04.2017.
 */

public interface WeightJournalContract {
    void addMeasurement (Measurement measurement);
    void deleteMeasurement (Measurement measurement, int itemPosition);
    List <Measurement> getAllMeasurements();
}
