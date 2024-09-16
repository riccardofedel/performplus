import { CruscottoModule } from './cruscotto.module';

describe('AmministratoriModule', () => {
    let amministratoriModule: CruscottoModule;

    beforeEach(() => {
        amministratoriModule = new CruscottoModule();
    });

    it('should create an instance', () => {
        expect(CruscottoModule).toBeTruthy();
    });
});
