import { ProgrammazioneModule } from './programmazione.module';

describe('ProgrammazioneModule', () => {
    let programmazioneModule: ProgrammazioneModule;

    beforeEach(() => {
        programmazioneModule = new ProgrammazioneModule();
    });

    it('should create an instance', () => {
        expect(ProgrammazioneModule).toBeTruthy();
    });
});
