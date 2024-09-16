
import { PerformanceModule } from 'src/app/performance/performance.module';

describe('RegistrazioniModule', () => {
    let registrazioniModule: PerformanceModule;

    beforeEach(() => {
        registrazioniModule = new PerformanceModule();
    });

    it('should create an instance', () => {
        expect(PerformanceModule).toBeTruthy();
    });
});
