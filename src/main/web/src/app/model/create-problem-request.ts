export class CreateProblemRequest {
    reasonGroupId: number;
    latitude: number;
    longitude: number;
    files: File[] = [];
    body: string;
}
