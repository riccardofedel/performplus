import { StampeModule } from './stampe.module';

describe('StampeModule', () => {
    let utentiModule: StampeModule;

    beforeEach(() => {
        utentiModule = new StampeModule();
    });

    it('should create an instance', () => {
        expect(StampeModule).toBeTruthy();
    });
});
