# Copilot Instructions for tlias-management

## Project Overview

**tlias-management** is a Vue 3 + TypeScript + Element Plus management system for handling employees, departments, classes, and students with role-based access control and persistent login state.

**Key Stack:**
- Vue 3 (Composition API with `<script setup>`)
- TypeScript 5.9
- Vite 7.2
- Element Plus 2.12 (UI components + icons)
- Pinia 3 (state management with persistence plugin)
- Axios (HTTP client with interceptors)
- ECharts 6 (data visualization for reports)

## Architecture Patterns

### API Layer (`src/api/`)

**Pattern:** Modular API files with TypeScript types

- Each entity has its own API file (`emp.ts`, `dept.ts`, `clazz.ts`, `stu.ts`, `login.ts`, `log.ts`, `analysis.ts`)
- All APIs use a centralized `request` axios instance configured at `src/utils/request.ts`
- API functions return typed responses using the `ResultModel<T>` or `PageResultModel<T>` interfaces
- Query parameters are passed as URL strings, not request objects (see `queryPageApi` pattern)
- Example: `request.get<any, PageResultModel>('/emps?page=${page}&pageSize=${pageSize}')`

**Type Definitions:** All shared models in `src/api/model/model.ts` with naming convention `{Entity}Model`

### HTTP Interceptors (`src/utils/request.ts`)

- **Request:** Automatically adds `token` header from `loginEmp` store
- **Response:** Extracts `data` from response envelope automatically
- **Error Handling:** 401 status triggers redirect to login page with `ElMessage.error()`
- **Base URL:** `/api` (backend API prefix)

### State Management (`src/stores/loginEmp.ts`)

- Uses Pinia's Composition API style with `ref` for reactive state
- Persists user login info to localStorage via `pinia-plugin-persistedstate`
- Store ID: `'loginEmpInfo'` - used for persistence key
- Methods: `setLoginEmp()`, `getLoginEmp()`, `removeLoginEmp()`
- Accessed in request interceptor to retrieve token

### Router Structure (`src/router/index.ts`)

- **Layout-based routing:** All authenticated routes nested under `layout/index.vue` parent
- **Root route `/` redirects to `index` child route**
- Main sections: `emp`, `dept`, `clazz`, `stu`, `log`, plus `/report` with nested `emp` and `stu` sub-routes
- Lazy-loaded components via `() => import('../views/...')`

### View Components (`src/views/`)

**Composition:** All follow similar patterns in `<script setup lang="ts">` blocks:

1. Import specific APIs from `src/api/{entity}.ts`
2. Define reactive state with `ref<TypeModel>()`
3. Define watch handlers for dependent data (e.g., date range → begin/end)
4. Call APIs in `onMounted()` or event handlers
5. Use pagination pattern: `ref<PaginationParams>({currentPage, pageSize, total})`
6. Update UI state after response with type-safe destructuring

**Example pattern from `src/views/emp/index.vue`:**
```typescript
const empList = ref<EmpModelArray>([])
const pagination = ref<PaginationParams>({currentPage: 1, pageSize: 10, total: 0})

const pageQuery = async () => {
  const result = await queryPageApi(...)
  if (result.code) {
    empList.value = result.data.rows
    pagination.value.total = result.data.total
  }
}
```

### Form Handling & Element Plus

- Uses Element Plus components: `ElMessage`, `ElMessageBox`, `FormInstance`, `FormRules`
- Import forms/components from `'element-plus'` (not individual .vue files)
- Type validation via `type FormRules = Record<string, FormRule[]>` pattern
- File upload handling via `UploadProps` type

## Developer Workflows

### Setup & Development

```bash
npm install                    # Install dependencies
npm run dev                    # Start dev server (hot-reload at :5173)
npm run type-check            # Vue TSC type checking
npm run lint                  # ESLint auto-fix
npm run format                # Prettier formatting
```

### Production Build

```bash
npm run build                 # Type-check, then build to dist/
npm run preview               # Preview production build locally
```

### Key Build Notes

- `npm run build` = `type-check` + `vite build` (fails if TS errors exist)
- TSX/JSX support enabled via `@vitejs/plugin-vue-jsx`
- Vue devtools integration via `vite-plugin-vue-devtools`

## Coding Conventions

### TypeScript & Types

- Strict typing required; use specific types like `EmpModel`, `DeptModel`, not `any`
- Suffix interfaces with `Model` (e.g., `EmpModel`, `PageResultModel`)
- Suffix arrays with `Array` (e.g., `EmpModelArray = EmpModel[]`)
- Always type API response with `<any, ExpectedResponseType>`

### Naming Conventions

- **API functions:** `{action}Api` or `{action}{Entity}Api` (e.g., `queryPageApi`, `addEmpApi`, `loginApi`)
- **Stores:** `use{Entity}Store` (e.g., `useLoginEmpStore`)
- **Models:** `{Entity}Model` (e.g., `EmpModel`, `DeptModel`)
- **Routes:** lowercase paths (e.g., `/emp`, `/dept`, `/clazz`)

### Watch Dependencies

- Use `watch()` with dependency array to sync computed UI state from input (see date-to-begin/end pattern)
- Update model before re-querying data

### Error Handling

- API errors automatically caught by response interceptor
- Success checks via `if (result.code)` (non-zero = success in this system)
- Use `ElMessage.error()` for user-facing errors from interceptor

### Element Plus Integration

- Chinese locale: `zhCn` from `'element-plus/es/locale/lang/zh-cn'`
- Icon components auto-registered globally from `@element-plus/icons-vue`
- Pagination: handle `size-change` and `current-change` events separately

## Common Patterns to Replicate

### Adding a New CRUD Feature

1. **Create API file** `src/api/new-entity.ts`:
   ```typescript
   export const queryPageApi = (...params) => request.get<any, PageResultModel>(...)
   export const addNewEntityApi = (data) => request.post<any, ResultModel>('/endpoint', data)
   ```

2. **Add types** to `src/api/model/model.ts` (e.g., `NewEntityModel`)

3. **Create view** `src/views/new-entity/index.vue`:
   - Import APIs and types
   - Define reactive state with `ref<TypeModel>()`
   - Implement pagination pattern
   - Call API on mount and after mutations

4. **Add route** to `src/router/index.ts` children array under layout

### Query String Parameters

Always build query strings inline in API calls:
```typescript
// DO: Build query string directly
request.get(`/emps?page=${page}&pageSize=${pageSize}&name=${name}`)

// DON'T: Use params object (not used in this project)
request.get('/emps', { params: {...} })
```

### Login State Flow

1. User logs in via `loginApi` → receives `token` + user info
2. Store saves via `setLoginEmp()` → auto-persisted to localStorage
3. All subsequent requests include token via request interceptor
4. On 401 error → interceptor redirects to login page
5. Logout: call `removeLoginEmp()` from store

## Debugging Tips

- **Type errors?** Run `npm run type-check` to see full TS errors
- **Hot reload issues?** Vite hot-reloads component script changes automatically
- **Persistent state stuck?** Clear localStorage or restart dev server
- **API 401s?** Check if `loginEmp` store has valid token (check browser DevTools)
- **Element Plus components missing?** Ensure imported from `'element-plus'`, not custom paths

## File Organization Philosophy

- API logic isolated from views (no fetch calls in components except via imported APIs)
- Single type definition source (`model.ts`) prevents duplication
- Router config centralized (no route-specific navigation imports needed)
- Stores focused on cross-component state only (not local component state)

