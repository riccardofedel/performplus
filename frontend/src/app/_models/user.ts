import { UserOperator } from "./operator";

export class User {
    username: string | undefined;
    token: string | undefined;
    operator: UserOperator | undefined;
}
